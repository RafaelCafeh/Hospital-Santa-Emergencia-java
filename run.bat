@echo off 
echo Compilando o projeto... 
javac -d . -cp ".;lib/*" Main.java estrutura\*.java interfaces\*.java console\*.java db\*.java 
if %errorlevel% neq 0 ( 
    echo. 
    echo Erro na compilacao! 
    exit /b %errorlevel% 
) 
echo Executando o projeto... 
java -cp ".;lib/*" Main 
