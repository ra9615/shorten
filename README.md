Сервис предназначен для сокращения ссылок с возможностью управления лимитом переходов и времени жизни ссылки.

Для начанала использования сервиса необходимо собрать и запустить в idea.
После запуска в консоли запустится интерактивная shell строка.
Для удобства пользователя можно ввести команду "help", которая выведет доступные команды и краткие примеры, как ими
пользоваться.
Лими переходов по ссылке и кол-во дней ее жизни заданы в файле application.properties
Для использования сервиса необходимо зарегистрироваться с помощью команды register, которая вернет пользователю его
уникальный идентификатор.
Его надо будет использлвать для авторизации при работе со ссылками.

Доступные команды:
- shorten: Команда укорачивает ссылку. На вход принимает оргинальную ссылку, идентификатор пользователя, также можно
  опционально задать лимит переходов по ссылке и сколько дней она будет активна. Возвращает пользователю короткий
  вариант ссылки.
  Пример: shorten originalUrl userId (опционально)maxClicks (опционально)maxDays
- edit: Устанавливает уже существующей ссылке лимит переходов. На вход принимает идентификатор пользователя, короткую
  ссылку, кол-во возможных переходов. Возвращает пользователю сообщение с ифнормацией об успехе/неудаче.
  Пример: userId shortUrl clickLimit
- go: Открывает в браузере сайт по короткой ссылке. На вход принимает идентификатор пользователя, короткую ссылку.
  Затем открывает данную ссылку в браузере.
  Пример: goSite userId shortUrl
- delete: Удяляет короткую ссылку. На вход принимает идентификатор пользователя, короткую ссылку. Возвращает
  пользователю сообщение с ифнормацией об успехе/неудаче.
  Пример: delete userId shortUrl
- register: Регистрирует нового пользователя. На вход ничего не принимает. Возвращает пользователю его уникальный
  идентификатор в системе.
  Пример: register

Для теста необходимо собрать и запустить проект.
Далее с помощью командной строки, появившейся во вкладке run в idea зарегистрировать пару пользователей. 
Далее протестировать функциональность приложения, используя различные сценарии: 
-Создать ссылку с несуществующим/существующим пользователем.
-Задать лимит переходов с пользователем, которому принадлежит/не принадлежит эта короткая ссылка.
-Удалить ссылку с пользователем, которому принадлежит/не принадлежит эта короткая ссылка.
-Задать лимит переходов и превысить его.
-Для теста срока действия ссылки придется в коде в методе validUrl задать дату из будущего.
