# ---- Stage 1: build static assets ----
FROM node:20-alpine AS build
WORKDIR /workspace

# Copy package files first for dependency caching.
COPY package.json package-lock.json ./
RUN npm ci

COPY . .
RUN npm run build

# ---- Stage 2: nginx serves the dist/ output ----
FROM nginx:1.27-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /workspace/dist /usr/share/nginx/html
EXPOSE 80