
package com.francescoz.downhill.screens.menu.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.net.HttpRequestBuilder;

public abstract class Online {

	private final HttpRequestBuilder requestBuilder;
	private HttpRequest request;
	private final HttpResponseListener listener;

	public Online() {
		requestBuilder = new HttpRequestBuilder();
		listener = new HttpResponseListener() {

			@Override
			public void cancelled() {
			}

			@Override
			public void failed(Throwable t) {
				response(-1);
			}

			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				response(666);
			}
		};
	}

	public void cancel() {
		if (request != null) {
			Gdx.net.cancelHttpRequest(request);
			request.reset();
			request = null;
		}
	}

	public void request(final int seed, float localX) {
		cancel();
		request = requestBuilder.newRequest().method(HttpMethods.GET).url("http://www.google.it").build();
		Gdx.net.sendHttpRequest(request, listener);
	}

	public abstract void response(float onlineX);

}
