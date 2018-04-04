/*
 * Copyright 2018. M. Reza Nasirloo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mrezanasirloo.slick.uni;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * @author : M.Reza.Nasirloo@gmail.com
 *         Created on: 2018-03-11
 */

public class SlickPresenterUniExample extends SlickPresenterUni<ViewExample, ViewStateExample> {

    public SlickPresenterUniExample(Scheduler main, Scheduler io) {
        super(main, io);
    }

    @Override
    protected void start(ViewExample view) {
        Observable<PartialViewState<ViewStateExample>> like = command(ViewExample::likeComment)
                .flatMap(aBoolean -> Observable.just(aBoolean).subscribeOn(io))//call to backend
                .map(PartialViewStateLiked::new);

        Observable<PartialViewState<ViewStateExample>> loadText = command(ViewExample::loadText)
                .flatMap(aBoolean -> Observable.just("Foo Bar").subscribeOn(io))//call to backend
                .map(PartialViewStateText::new);

        reduce(new ViewStateExample(null, false), merge(like, loadText)).subscribe(this);

    }

    @Override
    protected void render(@NonNull ViewStateExample state, @NonNull ViewExample view) {
        System.out.println("state = [" + state + "]");
        if (state.text() != null) view.setText(state.text());
        view.setLike(state.isLiked());
    }
}