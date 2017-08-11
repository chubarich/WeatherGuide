package com.example.julia.weatherguide.domain.use_cases;

import com.example.julia.weatherguide.data.entities.presentation.location.Location;
import com.example.julia.weatherguide.data.entities.presentation.location.LocationWithId;
import com.example.julia.weatherguide.data.entities.presentation.weather.Weather;
import com.example.julia.weatherguide.data.repositories.weather.OpenWeatherMapRepository;
import com.example.julia.weatherguide.data.repositories.weather.WeatherRepository;
import com.example.julia.weatherguide.domain.use_cases.base.SingleUseCase;
import com.example.julia.weatherguide.utils.Preconditions;

import static com.example.julia.weatherguide.data.repositories.weather.OpenWeatherMapRepository.GetWeatherStrategy;
import static com.example.julia.weatherguide.domain.use_cases.GetWeatherUseCase.Args;
import io.reactivex.Scheduler;
import io.reactivex.Single;


public class GetWeatherUseCase extends SingleUseCase<Weather, Args> {

    private final WeatherRepository weatherRepository;

    public GetWeatherUseCase(Scheduler worker, Scheduler postExecution, WeatherRepository weatherRepository) {
        super(worker, postExecution);
        Preconditions.nonNull(weatherRepository);
        this.weatherRepository = weatherRepository;
    }

    @Override
    protected Single<Weather> getUseCaseSingle(Args args) {
        return weatherRepository.getWeather(args.locationWithId, args.getWeatherStrategy);
    }


    public static class Args {

        public final LocationWithId locationWithId;
        public final GetWeatherStrategy getWeatherStrategy;

        public Args(LocationWithId locationWithId, GetWeatherStrategy getWeatherStrategy) {
            Preconditions.nonNull(locationWithId, getWeatherStrategy);
            this.locationWithId = locationWithId;
            this.getWeatherStrategy = getWeatherStrategy;
        }
    }
}
