

a="1 5 10 15 20 25 30 35 40 45 50 55 60 65 70 75"
echo "Values" >> Client_Threading_1.txt
for number in `echo $a` 
do
        echo "Doing $number clients for option 1"
	java Client_Threading 192.168.100.106 $number 4 >>  Client_Threading_4.txt
    
done
echo "We are finished"
