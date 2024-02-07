# ProjectPenZipper Deployment Guide

War File Is Present Inside This Folder

## Configure log4j.properties
Embark on a seamless deployment journey for your web project with these exhilarating configurations. Dive into the heart of your project at /WEB-INF/classes/log4j.properties and tailor your log file path to perfection.

```properties
# FILE PATH HERE FOR LOGS
log4j.appender.RollingAppender.File=/mnt/8EFED7B1FED79037/UBUNTU-BACKUP/Desktop/project-pen-zipper-logs/zipper.log 
```
## Update env.path for Utilities   
Unleash the power of utility configurations by navigating to /WEB-INF/classes/com/pp/util/. Open the config.properties file and revitalize the env.path.
```properties
env.path=ProjectPenZipperEnv/.env
```
Embrace precision with the timer cron in seconds—🔥no decimals allowed!🔥
```properties
# Timer Cron In Seconds (DO NOT GIVE IN DECIMALS) {"86400"s For Every Day}
timer.period=120
timer.delay=120
```
## Create .env File   
Ignite the final spark by creating a mystic .env file in the root directory post-deployment. Unveil its secrets with a touch of magic:
```properties
REPO_PATH=full path of rep like (/mnt/8EFED7B1FED79037/UBUNTU-BACKUP/VS-GIT/codepen)
USERNAME=username
PASSWORD=tokenhere
```

## Support
For inquiries and consultations, let the magic happen:
📧 **Email:** [umair2101f@aptechgdn.net](mailto:umair2101f@aptechgdn.net)
🔗 **LinkedIn:** [https://www.linkedin.com/in/umairalibhutto/](https://www.linkedin.com/in/umairalibhutto/)
Feel the enchantment as your project unfolds its wings! 🚀✨

