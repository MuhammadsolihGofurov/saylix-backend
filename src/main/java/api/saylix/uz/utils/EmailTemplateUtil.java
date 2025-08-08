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
                greeting = "当サイトへようこそ！";
                instruction = "登録を完了するには、以下のコードを使用してください。";
                ignoreMsg = "もしこの登録をリクエストしていない場合は、このメールを無視してください。";
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

    public static String buildPasswordResetEmailBody(String code, AppLanguage language) {
        String greeting;
        String instruction;
        String ignoreMsg;

        switch (language) {
            case uz -> {
                greeting = "Parolni tiklash so‘rovi";
                instruction = "Parolingizni tiklash uchun quyidagi koddan foydalaning:";
                ignoreMsg = "Agar siz parolni tiklashni so‘ramagan bo‘lsangiz, ushbu xabarni e'tiborsiz qoldiring.";
            }
            case ru -> {
                greeting = "Запрос на сброс пароля";
                instruction = "Чтобы сбросить пароль, используйте код ниже:";
                ignoreMsg = "Если вы не запрашивали сброс пароля, просто проигнорируйте это сообщение.";
            }
            case en -> {
                greeting = "Password Reset Request";
                instruction = "Use the code below to reset your password:";
                ignoreMsg = "If you did not request a password reset, please ignore this email.";
            }
            case jp -> {
                greeting = "パスワードリセットのリクエスト";
                instruction = "パスワードをリセットするには、以下のコードを使用してください。";
                ignoreMsg = "パスワードリセットをリクエストしていない場合は、このメールを無視してください。";
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

    public static String buildUpdateUsernameBody(String code, AppLanguage language) {
        String greeting;
        String instruction;
        String ignoreMsg;

        switch (language) {
            case uz -> {
                greeting = "Username o'zgartirish uchun so'rov";
                instruction = "Usernameni o'zgartirish uchun quyidagi koddan foydalaning:";
                ignoreMsg = "Agar siz usernameni o'zgartirishni so‘ramagan bo‘lsangiz, ushbu xabarni e'tiborsiz qoldiring.";
            }
            case ru -> {
                greeting = "Запрос на изменение имени пользователя";
                instruction = "Используйте следующий код для изменения имени пользователя:";
                ignoreMsg = "Если вы не запрашивали изменение имени пользователя, просто проигнорируйте это сообщение.";
            }
            case en -> {
                greeting = "Username Change Request";
                instruction = "Use the code below to change your username:";
                ignoreMsg = "If you did not request a username change, please ignore this message.";
            }
            case jp -> {
                greeting = "ユーザー名変更のリクエスト";
                instruction = "ユーザー名を変更するには、以下のコードを使用してください。";
                ignoreMsg = "もしユーザー名の変更をリクエストしていない場合は、このメッセージを無視してください。";
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

