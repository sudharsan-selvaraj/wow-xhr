import {
    WowXhrMock,
    XhookRequest,
    XhookResponse, XhrInterceptor
} from "../types";

class XhrMock implements XhrInterceptor {

    private _mocks: WowXhrMock[] = [];
    private _isPaused: Boolean = false;

    private findMock(options: { method: string, url: string }): WowXhrMock {
        return this._mocks
            .filter(m => {
                return m.method === options.method &&
                    new RegExp(m.url).test(options.url)
            })[0]
    }

    registerMock(mock: WowXhrMock) {
        this._mocks.push(mock);
    }

    pause() {
        this._isPaused = true;
    }

    resume() {
        this._isPaused = false;
    }

    beforeXHR(request: XhookRequest): Promise<void> {
        let self = this;
        return new Promise(function (resolve, reject) {
            let {method, url} = request;
            let mock = self.findMock({method, url});
            if (mock && mock.mockRequest) {
                if (mock.mockRequest.headers) {
                    Object.assign(request.headers, mock.mockRequest.headers)
                }

                if (mock.mockRequest.body && new RegExp(method).test("POST|PUT|PATCH")) {
                    request.body = mock.mockRequest.body;
                }
            }
            resolve();
        })
    }

    afterXHR(request: XhookRequest, response: XhookResponse): Promise<void> {
        let self = this;
        return new Promise(function (resolve, reject) {
            let {method, url} = request;
            let mRequest = self.findMock({method, url});
            if (mRequest && mRequest.mockResponse) {
                if (mRequest.mockResponse.headers) {
                    Object.assign(response.headers, mRequest.mockResponse.headers)
                }

                if (mRequest.mockResponse.body) {
                    response.text = response.data = mRequest.mockResponse.body;
                }

                if (mRequest.mockResponse.status) {
                    response.status = mRequest.mockResponse.status;
                }

                if (mRequest.mockResponse.delay) {
                    return setTimeout(resolve, mRequest.mockResponse.delay * 1000)
                }
            }
            resolve();
        })
    }

}

export {
    XhrMock
}