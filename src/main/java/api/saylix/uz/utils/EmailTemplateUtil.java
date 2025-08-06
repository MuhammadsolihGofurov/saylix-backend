package api.saylix.uz.utils;

import api.saylix.uz.enums.AppLanguage;

public class EmailTemplateUtil {

    public static String buildParagraph(String content) {
        return "<p style='font-family: Arial, sans-serif; font-size: 16px; line-height: 1.6;'>" + content + "</p>";
    }

    public static String buildRegistrationEmailBody(String code, AppLanguage language) {
        String greeting;
        String instruction;
        String ignoreMsg;

        switch (language) {
            case uz -> {
                greeting = "Saytimizga xush kelibsiz!";
                instruction = "Ro‘yxatdan o‘tishni yakunlash uchun quyidagi koddan foydalaning:";
                ignoreMsg = "Agar siz ro‘yxatdan o‘tishni so‘ramagan bo‘lsangiz, ushbu xabarni e'tiborsiz qoldiring.";
            }
            case ru -> {
                greeting = "Добро пожаловать на наш сайт!";
                instruction = "Чтобы завершить регистрацию, используйте код ниже:";
                ignoreMsg = "Если вы не запрашивали регистрацию, просто проигнорируйте это сообщение.";
            }
            case en -> {
                greeting = "Welcome to our website!";
                instruction = "Use the code below to complete your registration:";
                ignoreMsg = "If you did not request this, please ignore this email.";
            }
            case jp -> {
                greeting = "Welcome to our website! jp";
                instruction = "Use the code below to complete your registration: jp";
                ignoreMsg = "If you did not request this, please ignore this email. jp";
            }
            default -> throw new IllegalStateException("Unsupported language: " + language);
        }

        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #f4f4f4;
                            padding: 20px;
                        }
                        .container {
                            background-color: #ffffff;
                            max-width: 600px;
                            margin: 0 auto;
                            padding: 30px;
                            border-radius: 8px;
                            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                        }
                        .header {
                            font-size: 24px;
                            color: #333333;
                            margin-bottom: 10px;
                        }
                        .code {
                            font-size: 32px;
                            font-weight: bold;
                            color: #007BFF;
                            margin: 20px 0;
                        }
                        .footer {
                            font-size: 14px;
                            color: #888888;
                            margin-top: 30px;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">%s</div>
                        %s
                        <div class="code">%s</div>
                        %s
                        <div class="footer">© 2025 OurCompany. All rights reserved.</div>
                    </div>
                </body>
                </html>
                """.formatted(
                greeting,
                buildParagraph(instruction),
                code,
                buildParagraph(ignoreMsg)
        );
    }
}

