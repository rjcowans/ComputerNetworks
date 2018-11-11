

a="75 70 65 60 55 50 45 40 45 35 30 25 20 15 10 5 1"
#echo "Values" >> Client_Threading_1.txt
for number in `echo $a` 
do
        echo "Doing $number clients for option 1"
	java Client_Threading 192.168.100.106 $number 4 >>  Client_Threading_4.txt
	wait
    
done
echo "We are finished"
