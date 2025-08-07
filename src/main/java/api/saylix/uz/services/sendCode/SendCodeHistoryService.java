package api.saylix.uz.services.sendCode;

import api.saylix.uz.dto.utils.AppResponse;
import api.saylix.uz.entity.SendCodeHistoryEntity;
import api.saylix.uz.enums.AppLanguage;
import api.saylix.uz.enums.SendCode.CodeType;
import api.saylix.uz.enums.SendCode.TypeOfSendCodeProvider;
import api.saylix.uz.exps.AppBadException;
import api.saylix.uz.repository.sendCode.SendCodeHistoryRepository;
import api.saylix.uz.services.AppLanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SendCodeHistoryService {

    @Autowired
    private SendCodeHistoryRepository sendCodeHistoryRepository;

    @Autowired
    private AppLanguageService getLanguage;

    public void create(String username, String message, String code, TypeOfSendCodeProvider typeOfProvider, CodeType codeType) {
        SendCodeHistoryEntity sms = new SendCodeHistoryEntity();
        sms.setUsername(username);
        sms.setTypeOfProvider(typeOfProvider);
        sms.setMessage(message);
        sms.setCodeType(codeType);
        sms.setCode(code);
        sms.setAttemptCount(0);
        sms.setCreatedAt(LocalDateTime.now());

        sendCodeHistoryRepository.save(sms);
    }

    public Long getSmsCount(String username) {
        LocalDateTime nowDate = LocalDateTime.now();
        return sendCodeHistoryRepository.countByUsernameAndCreatedAtBetween(username, nowDate.minusMinutes(1), nowDate);
    }

    public AppResponse<String> check(String username, String code, AppLanguage language) {
        Optional<SendCodeHistoryEntity> optional = sendCodeHistoryRepository.findTop1ByUsernameOrderByCreatedAtDesc(username);

        if (optional.isEmpty()) {
            throw new AppBadException(getLanguage.getMessage("username.wrong", language));
        }

        SendCodeHistoryEntity sms = optional.get();

        //  urunishlar soni tugadi
        if (sms.getAttemptCount() >= 3) {
            throw new AppBadException(getLanguage.getMessage("send.code.many.times", language));
        }


        if (!code.equals(sms.getCode())) {
            sendCodeHistoryRepository.updateAttemptCount(sms.getId());

            throw new AppBadException(getLanguage.getMessage("code.is.wrong", language));
        }


        LocalDateTime expDate = sms.getCreatedAt().plusMinutes(2);
        if (LocalDateTime.now().isAfter(expDate)) {
            throw new AppBadException(getLanguage.getMessage("code.expired", language));
        }

        return new AppResponse<>(getLanguage.getMessage("code.success", language));
    }

}
