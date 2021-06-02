import {
    WowXhrMock,
    XhookRequest,
    XhookResponse, XhrInterceptor
} from "../types";

var MOCK_STORAGE_KEY = "_wow_xhr_mock_"
var MOCK_PAUSED_STORAGE_KEY = "_wow_xhr_mock_paused_"
var WINDOW: any = window;

class XhrMock implements XhrInterceptor {

    constructor() {
        try {
            if (!WINDOW.sessionStorage.getItem(MOCK_STORAGE_KEY)) {
                WINDOW.sessionStorage.setItem(MOCK_STORAGE_KEY, "[]");
            }
            if (!WINDOW.sessionStorage.getItem(MOCK_PAUSED_STORAGE_KEY)) {
                WINDOW.sessionStorage.setItem(MOCK_PAUSED_STORAGE_KEY, "false");
            }
        } catch (err) {
            console.log("Unable set session storage")
        }
    }

    private findMock(options: { method: string, url: string }): WowXhrMock[] {

        let mocks: WowXhrMock[] = JSON.parse(WINDOW.sessionStorage.getItem(MOCK_STORAGE_KEY) as any || "[]");
        if (WINDOW.sessionStorage.getItem(MOCK_PAUSED_STORAGE_KEY) == "true") {
            return;
        }
        return mocks
            .map((m: any) => JSON.parse(m))
            .filter((m: any) => {
                return m.method === options.method &&
                    new RegExp(m.url).test(options.url)
            })
    }

    beforeXHR(request: XhookRequest): Promise<void> {
        let self = this;
        return new Promise(function (resolve, reject) {
            let {method, url} = request;
            let mocks = self.findMock({method, url});
            mocks.forEach(function (mock) {

                if (mock && mock.mockRequest) {

                    let {headers, body, queryParams} = mock.mockRequest;
                    if (headers) {
                        Object.assign(request.headers, headers)
                    }

                    if (body && new RegExp(method).test("POST|PUT|PATCH")) {
                        request.body = body;
                    }


                    if (queryParams && Object.keys(queryParams).length) {
                        var parsedUrl = new URL(request.url);
                        parsedUrl.searchParams.forEach(function (val, key) {
                            if (queryParams.hasOwnProperty(key)) {
                                parsedUrl.searchParams.set(key, queryParams[key]);
                                delete queryParams[key];
                            }
                        })
                        Object.keys(queryParams).forEach(function (key) {
                            parsedUrl.searchParams.append(key, queryParams[key])
                        })
                        request.url = parsedUrl.toString();
                    }
                }
            })
            resolve();
        })
    }

    afterXHR(request: XhookRequest, response: XhookResponse): Promise<void> {
        let self = this;
        return new Promise(function (resolve, reject) {
            let {method, url} = request;
            let mocks = self.findMock({method, url});
            let totalDelay = 0;
            mocks.forEach(function (mock) {
                if (mock && mock.mockResponse) {
                    let {headers, body, status, delay} = mock.mockResponse;

                    if (headers) {
                        Object.assign(response.headers, headers)
                    }

                    if (body) {
                        response.text = response.data = body;
                    }

                    if (status) {
                        response.status = status;
                    }

                    if (delay) {
                        totalDelay = delay * 1000;
                    }
                }
            })

            setTimeout(resolve, totalDelay);
        })
    }

}

export {
    XhrMock
}