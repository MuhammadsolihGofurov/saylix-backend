package api.saylix.uz.entity;


import api.saylix.uz.enums.SendCode.CodeType;
import api.saylix.uz.enums.SendCode.TypeOfSendCodeProvider;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "send_code_history")
public class SendCodeHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "username")
    private String username;

    @Column(name = "type_of_provider")
    @Enumerated(EnumType.STRING)
    private TypeOfSendCodeProvider typeOfProvider;

    @Column(name = "message" , columnDefinition = "text")
    private String message;

    @Column(name = "code")
    private String code;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "code_type")
    @Enumerated(EnumType.STRING)
    private CodeType codeType;

    @Column(name = "attempt_count")
    private Integer attemptCount = 0;

}
