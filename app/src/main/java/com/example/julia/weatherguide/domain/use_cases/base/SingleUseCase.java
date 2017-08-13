package com.example.julia.weatherguide.domain.use_cases.base;


import com.example.julia.weatherguide.utils.Preconditions;

import io.reactivex.Scheduler;
import io.reactivex.Single;

public abstract class SingleUseCase<T, Params> {

    private final Scheduler workerScheduler;
    private final Scheduler postExecutionScheduler;

    protected SingleUseCase(Scheduler workerScheduler, Scheduler postExecutionScheduler) {
        Preconditions.nonNull(workerScheduler, postExecutionScheduler);
        this.workerScheduler = workerScheduler;
        this.postExecutionScheduler = postExecutionScheduler;
    }

    public Single<T> execute(Params params) {
        return wrapWithSchedulers(getUseCaseSingle(params));
    }

    protected abstract Single<T> getUseCaseSingle(Params params);

    private Single<T> wrapWithSchedulers(Single<T> single) {
        return single.subscribeOn(workerScheduler)
            .observeOn(postExecutionScheduler);
    }

}
