	.data
a:
	70
	80
	40
	20
	
n:
	4
	.text
main:
    load %x0, $n, %x3
    subi %x3, 1, %x3
while1:
    beq %x3, %x0, exit
    addi %x0, 0, %x4
while2:
    beq %x4, %x3, outer
    addi %x4, 1, %x5
    load %x4, $a, %x6
    load %x5, $a, %x7
    blt %x6, %x7, swap
    addi %x4, 1, %x4
    jmp while2
swap:
    store %x6, $a, %x5
    store %x7, $a, %x4
    addi %x4, 1, %x4
    jmp while2
outer:
    subi %x3, 1, %x3
    jmp while1
exit:
    end