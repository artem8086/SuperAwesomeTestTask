# **Super Awesome Test Task**

Фреймворк: **spring-boot** *2.2.6.RELEASE*.
База данных: **MongoDB**.

## Коллекции базы данных
 - User - Представляет данные о пользователях в приложение. Содержит данные для аутентификации, а также права пользовотеля (пользователь/администратор). Представлено моделью: `art.soft.test.model.User`
 - Post - Представляет данные о постах. Содержит заголовок, содержание, время публикации и ссылку на пользователя, сделавшего пост. Представлено моделью: `art.soft.test.model.Post`
 - Verify - Служит для подверждения регестрации пользователей. Хранит ссылку на пользователя, уникальный ключ который передается в ссылке для потверждения регистрации. После подтверждения регистрации, данные удаляються. Представлено моделью: `art.soft.test.model.VerificationToken`.

## Запросы для регистрации аккаунта
> В пути запроса * означает путь к приложению. Например: *http://localhost:8080/*
> В квадратных скобках будет указан тип запроса, если таковой требуеться. Например `[POST]` или `[GET]`

 - */account/signup `[POST]` - Регистрация пользователя. Параметры:
   + *login* - Логин пользователя (должен быть уникальным)
   + *email* - Email адрес пользователя (должен быть уникальным)
   + *password* - Пароль пользователя.

   После успешного выполнения данного запроса, будет создана неактивная учётная запись, а также будет возвращена строка содержащая ссылку подверждения регистрации, примерно такого вида: *http://localhost:8080/account/confirm?token=507f1f77bcf86cd799439011*.

 - */account/confirm - Подверждение регестрации пользователя. Параметры:
   + *token* - Токен сгенерированный при регистрации пользователя.

   После успешного выполнения данного запроса, учётная запись пользователя будет активирована и будет возвращен JSON обьект содержащий JWT токен для аутентификации пользователя. Например: `{ "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.
eyJzdWIiOiIxMjM0NSIsIm5hbWUiOiJKb2huIEdvbGQiLCJhZG1pbiI6dHJ1ZX0K.
LIHjWCBORSWMEibq-tnT8ue_deUqZx1K0XxCOXZRrBI" }`.

 - */account/signin `[POST]` - Вход пользователя. Пользователь должен быть зарегестривован и активирован в системе. Параметры:
   + *login* - Логин пользователя
   + *password* - Пароль пользователя.

   После успешного выполнения данного запроса будет возвращен JSON обьект содержащий JWT токен.

## Запросы пользователя
Для выполнения следующих запросов, нужно быть аутентифицированным в системе. Для этого следует в заголовке запроса (в `Header`), в поле `Authorization` указать токен аутентификации пользователя, вида `Token eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.
eyJzdWIiOiIxMjM0NSIsIm5hbWUiOiJKb2huIEdvbGQiLCJhZG1pbiI6dHJ1ZX0K.
LIHjWCBORSWMEibq-tnT8ue_deUqZx1K0XxCOXZRrBI` (обязательно наличие префикса *Token*).
> `{userid}` в пути запроса интерпретируется как **id** пользователя либо как его login/username.

 - */user/all `[GET]` - Возвращает упрощенный список всех пользователей, содержит только *id* и имя полльзователя. Необходимо для подписки на новых пользователей. При успешном выполнении вернёт спискок вида:
 `[ { "id": "507f191e810c19729de860ea", "username": "testuser1" }, { "id": "507f191e810c19729de860ea", "username": "testuser2" } ]`

 - */user/info/{userid} `[GET]` - Возвращает упрощённую информацию о конкретном пользавателе. Необходимо для того чтобы узнать *id* либо *username* конкретного пользователя. Вернёт JSON обьект вида:
 `{ "id": "507f191e810c19729de860ea", "username": "testuser1" }`

 - */post/create `[POST]` - Создаёт пост. Параметры запроса:
   + *title* - Заголовок поста
   + *content* - Содержимое поста

   После успешного выполнения данного запроса будет возвращен JSON обьект данного поста, например:
   `{ "id": "507f191e810c19729de860ea", "title": "Test title", "content": "Test content.", "date": "*", "username": "testuser1" }`

 - */post/delete/{postid} `[POST]` - Удаляет пост с индефикатором *postid*. При этом удалёны могут быть посты только принадлежащие пользавателю. *Администратор с помощью данного запроса может удалять любые посты*. После успешного выполнения данного запроса будет возвращен JSON обьект удалённого поста
  
 - */post/modify/{postid} `[POST]` - Модифицирует пост указанный индефикатором *postid*. Параметры запроса:
   + *title* - Заголовок поста (необязательный параметр)
   + *content* - Содержимое поста (необязательный параметр)

   При этом модифицированы могут быть посты только принадлежащие пользавателю. *Администратор с помощью данного запроса может модифицировать любые посты*. После успешного выполнения данного запроса будет возвращен JSON обьект удалённого поста

 - */user/subscribe/{userid} `[POST]` - Выполняет подписку на пользавателя указаного в *userid*. После успешного выполнения данного запроса будет возвращена строка вида: `You successfully subscribe on testuser2!`
  
 - */user/unsubscribe/{userid} `[POST]` - Выполняет отписку от пользавателя указаного в *userid*. После успешного выполнения данного запроса будет возвращена строка вида: `You successfully unsubscribe on testuser2!`
 
 - */post/my `[GET]` - Возвращает список постов сделанных аутентифицированным пользователем

 - */post/feed `[GET]` - Возвращает список постов отсортированных в хронологическом порядке пользователей на которых подписан аутентифицированный пользователь. Параметры запроса:
   + *title* - Необязательный параметр, если указан то будут выбраны только те посты, заголовки которых совпадают с указанным в параметре.

## Запросы администратора
Администратором может быть только тот пользователь у которого в учётной записи пользователя флага *isAdmin* установлен в `true`.
> Флаг *isAdmin* может быть указан только вручную через базу данных

 - */admin/users `[GET]` - Возвращает список всех пользователей содержащий почти все поля учетной записи пользователя, кроме пароля. При успешном выполнении вернёт спискок вида:
 `[ { "id": "507f191e810c19729de860ea", "login": "admin", "email": "someemail@gmail.com", "isAdmin": "true", "isActive": "true", "subs": [] } ]`

 - */admin/user/{userid} `[GET]` - Возвращает информацию о пользователе указаном в *userid* содержащий почти все поля учетной записи пользователя, кроме пароля. При успешном выполнении вернёт JSON обьект учётной записи пользователя

 - */admin/delete/{userid} `[POST]` - Полностью удаляет учётную запись пользователя, указанную в userid. Будут также удалены все посты данного пользователя, выполнены отписки у всех пользователей, которые подписаны на данную учётную запись, а также удалены все токены верефикации связаны с данной учётной записью. При успешном выполнении вернёт JSON обьект удалённой учётной записи пользователя

 - */admin/modify/{userid} `[POST]` - Выаолняет модификацию учётной записи пользователя, указанную в userid.
   + *login* - Логин пользователя (необязательный параметр)
   + *email* - Email пользователя (необязательный параметр)
   + *password* - Пароль пользователя (необязательный параметр)
   + *active* - Необязательный параметр, принимает `true` либо `false`, активирует либо деактивирует учётную запись пользователя.
   При успешном выполнении вернёт JSON обьект модифицированной учётной записи пользователя

 - */post/all `[GET]` - Возвращает список всех постов сделанных пользователемя. *Данный функционал доступен, только администраторам!*