package com.slickpath.mobile.android.simple.vm.rx

import io.reactivex.rxjava3.plugins.RxJavaPlugins

import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Scheduler

import io.reactivex.rxjava3.schedulers.Schedulers

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.concurrent.Callable


class RxSchedulersRule : TestRule {
    override fun apply(base: Statement, description: Description?): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxAndroidPlugins.reset()
                RxJavaPlugins.reset()
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { s: Callable<Scheduler?>? -> Schedulers.trampoline() }
                RxJavaPlugins.setIoSchedulerHandler { s: Scheduler? -> Schedulers.trampoline() }
                try {
                    base.evaluate()
                } finally {
                    RxAndroidPlugins.reset()
                    RxJavaPlugins.reset()
                }
            }
        }
    }
}