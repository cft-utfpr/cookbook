# Livro de receitas inteligente

> Trabalho de Técnicas de Programação

## Autor

Caio Furlan Traebert - 2780046

## Instruções de uso

### Download

```sh
git clone https://github.com/cft-utfpr/cookbook.git
cd cookbook
```

### Build

**Linux**

```sh
./gradlew shadowJar # Para gerar tudo em um único .jar
```

**Windows**

```sh
./gradlew.bat shadowJar # Para gerar tudo em um único .jar
```

O arquivo .jar será gerado em `./app/build/libs/app-all.jar`.

### Utilização

Ao abrir o app com `java -jar ./app/build/libs/app-all.jar`, você encontrará a tela inicial perguntando seus ingredientes. Escreva os ingredientes que você quer utilizar e clique em Submit. Caso o app esteja sendo executado pela primeira vez, uma janela aparecerá pedindo uma chave API Groq, que será salva posteriormente em um txt ao lado do .jar. 
