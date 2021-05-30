import {XhookRequest, XhookResponse} from "../types";

class LogInfo {

    private _initiatedTime: number;
    private _completedTime: number;
    private _request: XhookRequest;
    private _response: XhookResponse;

    get initiatedTime(): number {
        return this._initiatedTime;
    }

    set initiatedTime(value: number) {
        this._initiatedTime = value;
    }

    get completedTime(): number {
        return this._completedTime;
    }

    set completedTime(value: number) {
        this._completedTime = value;
    }

    get request(): XhookRequest {
        return this._request;
    }

    set request(value: XhookRequest) {
        this._request = value;
    }

    get response(): XhookResponse {
        return this._response;
    }

    set response(value: XhookResponse) {
        this._response = value;
    }
}

export {
    LogInfo
}