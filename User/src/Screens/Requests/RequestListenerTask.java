package Screens.Requests;

import Dto.FullRequestDto;

import java.util.List;
import java.util.TimerTask;

public class RequestListenerTask extends TimerTask {

    RequestController requestController;
    List<FullRequestDto> currentRequests;
    public RequestListenerTask(RequestController requestController) {
        this.requestController = requestController;
        currentRequests = null;
    }

    @Override
    public void run() {
        List<FullRequestDto> requests = requestController.getListRequestsFromServer();
        if (requests != null){
            if (currentRequests == null || !currentRequests.equals(requests)){
                requestController.setCurrentRequests(requests);
                currentRequests = requests;
            }
        }

    }
}
