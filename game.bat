@echo off

REM Define o caminho para as bibliotecas nativas com base no sistema operacional
IF "%PROCESSOR_ARCHITECTURE%"=="AMD64" (
    SET NATIVE_PATH=lib/natives/windows
) ELSE (
    SET NATIVE_PATH=lib/natives/linux
)

REM Cria a pasta bin no diretório build se ela não existir
IF NOT EXIST bin (
    mkdir bin
)

REM Define o nome do arquivo de log
SET LOG_FILE=log.txt

REM Define a variável para controlar a opção de log (terminal ou arquivo)
SET LOG_OPTION=%2

REM Target "rb": Compila e executa o programa
IF "%1"=="run" (
    CALL %0 build %2
    CALL %0 justRun %2
    exit /b
)

REM Target "run": Executa o programa
IF "%1"=="justRun" (
    IF "%LOG_OPTION%"=="terminal" (
        echo Executando o programa...
        java -Djava.library.path=%NATIVE_PATH% -cp bin;lib/jars/lwjgl.jar;lib/jars/lwjgl_util.jar;lib/jars/slick-util.jar engineTester.MainGameLoop
        echo Execução concluída.
    ) ELSE (
        echo Executando o programa... >> %LOG_FILE%
        java -Djava.library.path=%NATIVE_PATH% -cp bin;lib/jars/lwjgl.jar;lib/jars/lwjgl_util.jar;lib/jars/slick-util.jar engineTester.MainGameLoop >> %LOG_FILE%
        echo Execução concluída. >> %LOG_FILE%
    )
    exit /b
)

REM Target "clean": Remove os arquivos compilados
IF "%1"=="clean" (
    IF "%LOG_OPTION%"=="terminal" (
        echo Limpando arquivos compilados...
        rmdir /s /q bin
        echo Limpeza concluída.
    ) ELSE (
        echo Limpando arquivos compilados... >> %LOG_FILE%
        rmdir /s /q bin >> %LOG_FILE% 2>&1
        echo Limpeza concluída. >> %LOG_FILE%
    )
    exit /b
)

REM Target "build": Compila os arquivos
IF "%1"=="build" (
    IF "%LOG_OPTION%"=="terminal" (
        echo Compilando arquivos...
        javac -d bin -cp lib/jars/lwjgl.jar;lib/jars/lwjgl_util.jar;lib/jars/slick-util.jar src/engineTester/MainGameLoop.java src/renderEngine/*.java src/shaders/*.java src/textures/*.java src/models/*.java src/toolBox/*.java src/entities/*.java
        echo Compilação concluída.
    ) ELSE (
        echo Compilando arquivos... >> %LOG_FILE%
        javac -d bin -cp lib/jars/lwjgl.jar;lib/jars/lwjgl_util.jar;lib/jars/slick-util.jar src/engineTester/MainGameLoop.java src/renderEngine/*.java src/shaders/*.java src/textures/*.java src/models/*.java src/toolBox/*.java src/entities/*.java>> %LOG_FILE% 2>&1
        echo Compilação concluída. >> %LOG_FILE%
    )
    exit /b
)

REM Se nenhum argumento válido foi passado, mostra uma mensagem de ajuda
echo Uso: %0 [justRun|clean|build|run] [terminal|log]
echo Exemplo: %0 rb terminal
exit /b
