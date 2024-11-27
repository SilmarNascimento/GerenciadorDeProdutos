# Usa a imagem Node.js para desenvolvimento
FROM node:19-alpine

# Diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia apenas os arquivos de dependência inicialmente
COPY package.json package-lock.json ./

# Instala as dependências do projeto
RUN npm install

# Copia o restante dos arquivos do projeto
COPY . .

# Expõe a porta do servidor de desenvolvimento Angular
EXPOSE 4200

# Comando para iniciar o servidor de desenvolvimento
CMD ["npm", "start"]

