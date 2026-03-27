@echo off
set "default_msg=push-git.bat work"

if "%1"=="" (
    set "commit_msg=%default_msg%"
) else (
    set "commit_msg=%*"
)

git add .
git commit -m "%commit_msg%"
git push origin main