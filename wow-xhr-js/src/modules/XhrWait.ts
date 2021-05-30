import {XhrLog} from "./XhrLog";

class XhrWait {

    constructor(private _xhrLog: XhrLog) {
    }

    waitForAllRequestsToComplete(waitTime: number, callback: () => any) {
        let self = this;
        setTimeout(async function () {
            while (self._xhrLog.getPendingXhrCalls().length) {
                await self._wait(1000)
            }
            callback()
        }, 1000)
    }

    _wait(ms: number) {
        return new Promise(function (resole, reject) {
            setTimeout(resole, ms);
        })
    }
}

export {
    XhrWait
}