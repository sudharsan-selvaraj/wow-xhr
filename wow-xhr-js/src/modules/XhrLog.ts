import {XhookRequest, XhookResponse, XhrInterceptor} from "../types";
import {LogInfo} from "./LogInfo";

class XhrLog implements XhrInterceptor {

    private _pendingXhrCalls: Map<String, LogInfo> = new Map();
    private _completedXhrCalls: LogInfo[] = [];

    beforeXHR(request: XhookRequest): Promise<void> {
        let xhrId = request.wow_xhr_id;
        let logInfo = new LogInfo();
        logInfo.request = request;
        logInfo.initiatedTime = Date.now();
        this._pendingXhrCalls.set(xhrId, logInfo);
        return Promise.resolve();
    }

    afterXHR(request: XhookRequest, response: XhookResponse): Promise<void> {
        let xhrId = request.wow_xhr_id;
        let logInfo: LogInfo = this._pendingXhrCalls.get(xhrId);

        logInfo.response = response;
        logInfo.completedTime = Date.now();

        this._completedXhrCalls.push(logInfo);
        this._pendingXhrCalls.delete(xhrId);
        return Promise.resolve(undefined);
    }

    getCompletedLogs() {
        return this._completedXhrCalls;
    }

    flushLogs() {
        return this._completedXhrCalls = [];
    }

    getPendingXhrCalls() {
        return Array.from(this._pendingXhrCalls.values());
    }
}

export {
    XhrLog
}