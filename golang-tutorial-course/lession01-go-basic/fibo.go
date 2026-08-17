package main

import (
	"fmt"
)

// 1	1	2	3	5	8	13	21
func fibo(number int) int {
	if number < 1 {
		return 0
	}
	if number == 1 || number == 2 {
		return 1
	}

	return fibo(number-1) + fibo(number-2)
}

func main() {
	var number int
	fmt.Printf("Nhap so fibo: ")
	_, error := fmt.Scan(&number)
	if error != nil || number <= 0 {
		fmt.Printf("Nhap lieu bi sai, vui long nhap lai: ")
	} else {
		sum := 0
		fiboNumber := 0
		for i := 1; i <= number; i++ {
			fiboNumber = fibo(i)
			sum += fiboNumber
		}
		fmt.Printf("so fibo thu %d la: %d va tong la %d ", number, fiboNumber, sum)
	}

}
