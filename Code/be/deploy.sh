echo "Building app..."
./mvnw clean package -Dmaven.test.skip=true #build ra file jar

echo "Deploy files to server..."
scp -r  target/be.jar root@159.223.89.187:/var/www/be/ #copy file jar day len server

ssh root@159.223.89.187 <<EOF
pid=\$(sudo lsof -t -i :8082)

if [ -z "\$pid" ]; then
    echo "Start server..."
else
    echo "Restart server..."
    sudo kill -9 "\$pid"
fi
cd /var/www/be
java -jar be.jar
EOF
exit
echo "Done!"