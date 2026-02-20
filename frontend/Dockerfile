# Stage 1: Build the Angular application
FROM node:22-alpine as build_stage

# Set the working directory
WORKDIR /app

# Copy package.json and package-lock.json to the working directory
COPY package.json package-lock.json* ./

# Clear npm cache and install dependencies with clean slate
RUN npm cache clean --force && \
    npm install --force

# Copy the rest of the application code
COPY . .

# Run build
RUN npm run build:production

# Stage 2: Serve the Angular application using nginx
FROM nginx:alpine

# Copy the built Angular application from the build_stage to nginx's web directory
COPY --from=build_stage /app/dist/sample-app-frontend/browser /usr/share/nginx/html
# Expose port 80
EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"]