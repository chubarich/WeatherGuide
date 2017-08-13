package com.example.julia.weatherguide.domain.use_cases.base;


import com.example.julia.weatherguide.utils.Preconditions;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public abstract class ObservableUseCase<T, Params> {

    private final Scheduler workerScheduler;
    private final Scheduler postExecutionScheduler;

    protected ObservableUseCase(Scheduler workerScheduler, Scheduler postExecutionScheduler) {
        Preconditions.nonNull(workerScheduler, postExecutionScheduler);
        this.workerScheduler = workerScheduler;
        this.postExecutionScheduler = postExecutionScheduler;
    }

    public Observable<T> execute(Params params) {
        return wrapWithSchedulers(getUseCaseObservable(params));
    }

    protected abstract Observable<T> getUseCaseObservable(Params params);

    private Observable<T> wrapWithSchedulers(Observable<T> observable) {
        return observable.subscribeOn(workerScheduler)
            .observeOn(postExecutionScheduler);
    }

}
