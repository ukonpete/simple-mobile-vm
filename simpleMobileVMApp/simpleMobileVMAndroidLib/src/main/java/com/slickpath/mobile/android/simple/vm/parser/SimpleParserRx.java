package com.slickpath.mobile.android.simple.vm.parser;

import android.support.annotation.NonNull;

import com.slickpath.mobile.android.simple.vm.FileHelperRx;
import com.slickpath.mobile.android.simple.vm.VMError;
import com.slickpath.mobile.android.simple.vm.VMErrorType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class SimpleParserRx extends SimpleParserBase{


    public SimpleParserRx(@NonNull FileHelperRx fileHelper, IParserListener listener) {
        super(fileHelper, listener);
    }

    @Override
    protected boolean doParse() throws VMError {
        final FileHelperRx fileHelper = (FileHelperRx)getFileHelper();
        Observable<String> observable = fileHelper.getInstructions();
        final AtomicInteger lineNumber = new AtomicInteger(0);
        final Map<String, Integer> symbols = new HashMap<>();
        final ArrayList<Integer> emptyList = new ArrayList<>(0);

        Single.create(new SingleOnSubscribe<String>() {
            @Override
            public void subscribe(SingleEmitter<String> e) throws Exception {

            }
        })

        if(observable != null) {
            observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(String line) {
                    int lastLine = doParseSymbolLine(lineNumber.get(), symbols, line);
                    lineNumber.set(lastLine);
                    doParseLine(line, emptyList);
                }

                @Override
                public void onError(Throwable e) {
                    getParserListener().completedParse(new VMError(e, VMErrorType.VM_ERROR_TYPE_UNKNOWN), getCommands());
                }

                @Override
                public void onComplete() {
                    getParserListener().completedParse(null, getCommands());
                }
            });
        }
        return false;
    }
}
