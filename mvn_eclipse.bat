cd iraj-bom
call mvn clean
call mvn install

cd ..\irajblank-parent
call mvn clean
call mvn eclipse:clean
call mvn eclipse:eclipse

cd ..

echo "End of script"

pause
