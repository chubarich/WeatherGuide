package com.example.julia.weatherguide.domain.use_cases.base;

import com.example.julia.weatherguide.utils.Preconditions;
import io.reactivex.Completable;
import io.reactivex.Scheduler;


public abstract class CompletableUseCase<Params>  {

    private final Scheduler workerScheduler;
    private final Scheduler postExecutionScheduler;

    protected CompletableUseCase(Scheduler workerScheduler, Scheduler postExecutionScheduler) {
        Preconditions.nonNull(workerScheduler, postExecutionScheduler);
        this.workerScheduler = workerScheduler;
        this.postExecutionScheduler = postExecutionScheduler;
    }

    public final Completable execute(Params params) {
        return wrapWithSchedulers(getUseCaseCompletable(params));
    }

    protected abstract Completable getUseCaseCompletable(Params params);

    private Completable wrapWithSchedulers(Completable completable) {
        return completable.subscribeOn(workerScheduler)
            .observeOn(postExecutionScheduler);
    }

}
