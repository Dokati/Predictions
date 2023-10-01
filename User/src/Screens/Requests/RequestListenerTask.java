package Screens.Requests;

import Dto.FullRequestDto;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.TimerTask;

import static Request.RequestCreator.*;

public class RequestListenerTask extends TimerTask {

    RequestController requestController;
    List<FullRequestDto> currentRequests;
    String UserName;
    public RequestListenerTask(RequestController requestController, String UserName) {
        this.requestController = requestController;
        currentRequests = null;
        this.UserName = UserName;
    }

    @Override
    public void run() {
        List<FullRequestDto> requests = getListRequestsFromServer();
        if (requests != null){
            if (currentRequests == null || !currentRequests.equals(requests)){
                requestController.setCurrentRequests(requests);
                currentRequests = requests;
            }
        }

    }

    public List<FullRequestDto> getListRequestsFromServer() {
        Request request = createGetListRequestsFromServer(UserName);
        Response response = ExecuteRequest(request);
        if (response != null) {
            try {
                Type FullRequestDtoListType = new TypeToken<List<FullRequestDto>>() {}.getType();
                List<FullRequestDto> FullRequestDtoList = new Gson().fromJson(response.body().string(), FullRequestDtoListType);
                return FullRequestDtoList;
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return null;
    }
}
