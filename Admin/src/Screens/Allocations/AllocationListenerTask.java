package Screens.Allocations;

import Dto.AdminRequestDto;
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

public class AllocationListenerTask extends TimerTask {

    List<AdminRequestDto> currentRequests;
    AllocationController allocationController;

    public AllocationListenerTask(AllocationController allocationController) {
        this.currentRequests = null;
        this.allocationController = allocationController;
    }

    @Override
    public void run() {
        List<AdminRequestDto> requests = getListRequestsFromServer();
        if (requests != null){
            if (currentRequests == null || !currentRequests.equals(requests)){
                allocationController.setCurrentAllocationRequests(requests);
                currentRequests = requests;
            }
        }
    }

    public List<AdminRequestDto> getListRequestsFromServer() {
        Request request = CreateGetRequest("admin");
        Response response = ExecuteRequest(request);
        if (response != null) {
            try {
                Type AdminRequestDtoListType = new TypeToken<List<AdminRequestDto>>() {}.getType();
                List<AdminRequestDto> AdminRequestDtoList = new Gson().fromJson(response.body().string(), AdminRequestDtoListType);
                return AdminRequestDtoList;
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return null;
    }
}
