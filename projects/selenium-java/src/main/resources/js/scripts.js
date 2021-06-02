/////////////////////////
// Mock related functions
//////////////////////////

function injectMock(newMocks) {
    if (new RegExp("^(data|about:)").test(document.location.toString())) {
        return;
    }

    var MOCK_STORAGE_KEY = "_wow_xhr_mock_";
    var existingMocks = JSON.parse(window.sessionStorage.getItem(MOCK_STORAGE_KEY) || "[]");
    var existingMockIds = {};
    existingMocks.forEach(function (eMock) {
        existingMockIds[JSON.parse(eMock).id] = true;
    });
    var mocksTobeAdded = existingMocks.concat(newMocks.filter((m) => !existingMockIds[JSON.parse(m).id]));
    window.sessionStorage.setItem(MOCK_STORAGE_KEY, JSON.stringify(mocksTobeAdded))
}

function pauseMocking() {
    window.sessionStorage.setItem("_wow_xhr_mock_paused_", "true");
}

function resumeMocking() {
    window.sessionStorage.setItem("_wow_xhr_mock_paused_", "false");
}

/////////////////////////
// Logs related functions
//////////////////////////

function getLogs(clearLogs) {
    let COMPLETED_CALLS_STORAGE_KEY = "_wow_xhr_log_completed_calls_";
    try {
        let logs = JSON.parse(window.sessionStorage.getItem(COMPLETED_CALLS_STORAGE_KEY || "[]"));
        if (clearLogs) {
            window.sessionStorage.setItem(COMPLETED_CALLS_STORAGE_KEY, "[]");
        }
        return logs;
    } catch (err) {
        return [];
    }
}

function clearLogs() {
    window.sessionStorage.setItem("_wow_xhr_log_completed_calls_", "[]");
}

/////////////////////////
// Utility functions
//////////////////////////

function waitForPageLoad() {
    return !(new RegExp("^(data|about:)").test(document.location.toString())) &&
        document.readyState == "complete";
}