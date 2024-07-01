echo "Building app..."
npm run build
echo "Deploy files to server..."
scp -r build/* root@104.248.224.6:/var/www/html
echo "Done!"