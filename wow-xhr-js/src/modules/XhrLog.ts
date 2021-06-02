import {XhookRequest, XhookResponse, XhrInterceptor} from "../types";
import {LogInfo} from "./LogInfo";

var LOG_PENDING_CALLS_STORAGE_KEY = "_wow_xhr_log_pending_calls_"
var LOG_COMPLETED_CALLS_STORAGE_KEY = "_wow_xhr_log_completed_calls_"
var WINDOW: any = window;

class XhrLog implements XhrInterceptor {
    constructor() {
        try {
            [LOG_PENDING_CALLS_STORAGE_KEY, LOG_COMPLETED_CALLS_STORAGE_KEY].forEach(function (key) {
                if (!WINDOW.sessionStorage.getItem(key)) {
                    WINDOW.sessionStorage.setItem(key, "[]");
                }
            })
        } catch (err) {
            console.log("Unable to set session storage")
        }
    }

    beforeXHR(request: XhookRequest): Promise<void> {
        let xhrId = request.wow_xhr_id;
        let logInfo = new LogInfo();

        logInfo.id = xhrId;
        logInfo.request = request;
        logInfo.initiatedTime = Date.now();
        logInfo.pending = true;
        var pendingCalls = JSON.parse(WINDOW.sessionStorage.getItem(LOG_PENDING_CALLS_STORAGE_KEY) || "[]");
        pendingCalls.push(logInfo)
        WINDOW.sessionStorage.setItem(LOG_PENDING_CALLS_STORAGE_KEY, JSON.stringify(pendingCalls));
        return Promise.resolve();
    }

    afterXHR(request: XhookRequest, response: XhookResponse): Promise<void> {
        let xhrId = request.wow_xhr_id;
        var pendingCalls = JSON.parse(WINDOW.sessionStorage.getItem(LOG_PENDING_CALLS_STORAGE_KEY) || "[]");
        let logIndex = pendingCalls.findIndex(call => call.id == xhrId);
        let logInfo: LogInfo = pendingCalls[logIndex];
        logInfo.response = response;
        logInfo.completedTime = Date.now();
        logInfo.pending = false;
        var completedCalls = JSON.parse(WINDOW.sessionStorage.getItem(LOG_COMPLETED_CALLS_STORAGE_KEY) || "[]");
        completedCalls.push(logInfo);

        this.setCompletedCalls(completedCalls);
        this.setPendingCalls(pendingCalls);
        return Promise.resolve(undefined);
    }

    setCompletedCalls(logs) {
        WINDOW.sessionStorage.setItem(LOG_COMPLETED_CALLS_STORAGE_KEY, JSON.stringify(logs));
    }

    setPendingCalls(logs) {
        WINDOW.sessionStorage.setItem(LOG_PENDING_CALLS_STORAGE_KEY, JSON.stringify(logs));
    }
}

export {
    XhrLog
}