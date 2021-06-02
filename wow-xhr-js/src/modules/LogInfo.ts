import {XhookRequest, XhookResponse} from "../types";

class LogInfo {
    public initiatedTime: number;
    public completedTime: number;
    public id: string;
    public request: XhookRequest;
    public response: XhookResponse;
    public pending:boolean;
}

export {
    LogInfo
}