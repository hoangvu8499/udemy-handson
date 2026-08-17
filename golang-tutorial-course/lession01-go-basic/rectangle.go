package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

type rectangle struct {
	long float32 `long: chieu dai`
	wide float32 `wide: chieu rong`
}

func (rec *rectangle) area() float32 {
	return rec.long * rec.wide
}

func (rec *rectangle) perimeter() float32 {
	return (rec.long + rec.wide) * 2
}

func readFloat(prompt string) (float32, error) {
	fmt.Print(prompt)
	reader := bufio.NewReader(os.Stdin)
	input, err := reader.ReadString('\n')
	if err != nil {
		return 0, fmt.Errorf("Error reading input: %v", err)
	}

	input = strings.TrimSpace(input)
	numb, err := strconv.ParseFloat(input, 32)
	if err != nil {
		return 0, fmt.Errorf("Error reading input: %v", err)
	}
	return float32(numb), nil
}

func inputRecData() rectangle {
	var long float32
	var wide float32

	for {
		for {
			var err error
			long, err = readFloat("Please input data Rectangle's LONG:")
			if err != nil || long <= 0 {
				fmt.Printf("Please input data for Rectangle's LONG is a number and > 0 \n")
			} else {
				break
			}
		}

		for {
			var err error
			wide, err = readFloat("Please input data Rectangle's WIDE:")
			if err != nil || wide <= 0 {
				fmt.Printf("Please input data for Rectangle's WIDE is a number and > 0 \n")
			} else {
				break
			}
		}
		if long <= wide {
			fmt.Printf("Long should be more than Wide \n")
		} else {
			break
		}
	}

	return rectangle{long, wide}
}

func main() {

	rectangle := inputRecData()

	fmt.Printf("Rectangle have long= %v and wide= %v \n", rectangle.long, rectangle.wide)
	fmt.Printf("Rectangle's perimeter: %v \n", rectangle.perimeter())
	fmt.Printf("Rectangle's area: %v \n", rectangle.area())

}
