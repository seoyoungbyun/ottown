package controller.OTTGroup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import controller.Controller;
import model.dao.OTTGroup.OTTGroupDao;
import model.domain.OTTService;
import model.domain.OTTGroup;
import model.domain.Consumer;
import model.service.OTTGroupMemberManager;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ShowOTTGroupListController implements Controller {
    private OTTGroupDao ottGroupDao = new OTTGroupDao();
    private OTTGroupMemberManager ottGroupMemberManager = new OTTGroupMemberManager();  // Manager 클래스

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");

        // 요청 파라미터에서 ottId 가져오기
        String ottIdParam = request.getParameter("ottId");

        // ottId가 없거나 유효하지 않은 경우 처리
        if (ottIdParam == null || ottIdParam.isEmpty()) {
            request.setAttribute("errorMessage", "OTT ID가 없습니다.");
            return "/errorPage.jsp";
        }

        int ottId;
        try {
            ottId = Integer.parseInt(ottIdParam);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "잘못된 OTT ID 형식입니다.");
            return "/errorPage.jsp";
        }

        // ottId로 OTTService 찾기
        OTTService ottService = null;
        for (OTTService service : OTTService.values()) {
            if (service.getId() == ottId) {
                ottService = service;
                break;
            }
        }

        if (ottService == null) {
            request.setAttribute("errorMessage", "유효하지 않은 OTT 서비스 ID입니다.");
            return "/errorPage.jsp";
        }

        // DAO를 통해 OTT 그룹 목록 가져오기
        List<OTTGroup> groupList = ottGroupDao.getOTTGroupsByOttService(ottService);

        // 각 그룹의 호스트 이름 추가
        Map<Integer, String> hostNames = new HashMap<>();
        for (OTTGroup group : groupList) {
            try {
                // groupId로 role = true인 멤버의 consumerId 찾기
                Long consumerId = ottGroupMemberManager.getConsumerIdByRoleTrue(group.getId());
                if (consumerId != null) {
                    // consumerId로 Consumer 정보 조회
                    Consumer consumer = ottGroupMemberManager.getConsumerById(consumerId);
                    if (consumer != null) {
                        hostNames.put(group.getId(), consumer.getConsumerName());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(); // 오류 처리 (호스트 정보가 없는 경우)
                hostNames.put(group.getId(), "알 수 없음");
            }
        }

        // JSP로 데이터 전달
        request.setAttribute("ottGroupList", groupList);
        request.setAttribute("ottService", ottService);
        request.setAttribute("hostNames", hostNames); // 그룹별 호스트 이름 전달

        return "/ottGroupList/OTTGroupList.jsp";  // 데이터가 전달된 후 JSP로 이동
    }
}
