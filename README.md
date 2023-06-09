# Что это?
Это Телеграм бот, который уведомляет вас о новых изменениях на сайте. На текущий момент бот способен отслеживать репозитории в GitHub и вопросы на StackOverFlow, но вы не ограничены и можете самостоятельно добавить новые сайты. (это не трудно, ниже будет инструкция как это сделать)

# Что под капотом?
* Spring Boot 3
* [SDK Telegram Bot API](https://github.com/pengrad/java-telegram-bot-api "https://github.com/pengrad/java-telegram-bot-api")
* Hibernate, JdbcTemplate, JOOQ (для каждого написаны свои repositories и servises)
* PostgreSQL 15
* Liquibase
* RabbitMQ 3
* JUnit
* Mockito
* Prometheus
* Grafana

# Какой функционал?
Вот небольшое видео, как работает бот:

https://github.com/Neonik-dev/Update-Tracker-Bot/assets/64394145/7bfcd748-6c67-47e1-b5d1-ccd1caf00e2b


На текущий момент в репозиториях GitHub отслеживаются следующие изменения:
* Время последнего изменения
* Commits
* Branches
* Issue
* Комментарии в issue
* Pull requests

В StackOverFlow пока отслеживается только время последнего изменения.

# Хочу больше сайтов и больше подробностей об обновлении, что нужно сделать?
Чтобы добавить новый сайт вам необходимо дописать следующее:

