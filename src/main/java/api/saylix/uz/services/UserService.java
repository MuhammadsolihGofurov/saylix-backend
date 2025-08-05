package api.saylix.uz.services;


import api.saylix.uz.dto.auth.RegisterUserDTO;
import api.saylix.uz.enums.AppLanguage;
import api.saylix.uz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String registrationUser(RegisterUserDTO dto, AppLanguage lang) {
//        user check qilamiz agar bo'lsa, bor diymiz, agar in_registrationda bo'lsa delete qilib qo'yamiz userni va keyingi stepga o'tadi


//        userni create qilamiz Role bo'yicha Teacher, Student ekanligini aniqlab shu tablelar bo'yicha ham create qilamiz


//        userga email orqali kod yuboramiz  (ikki xil logika qilamiz email va phone_number uchun hozircha email ishlatiladi)

        return "success";
    }


}
