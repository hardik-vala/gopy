/* Edge of integer overflow. */

package main

func main() {
	x := 1
	for i := 0 ; i < 32; i++ {
		x *= 2
	}

	print(x)
}
