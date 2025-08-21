    .data
n:
	10
	.text
main:
    load %x0, $n, %x3
    addi %x0, 65535, %x4
    addi %x0, 0, %x5
    addi %x0, 1, %x7
loop:
    store [%x5], 0, [%x4]
    subi %x4, 1, %x4
    subi %x3, 1, %x3
    add %x5, %x7, %x8
    addi %x7, 0, %x5
    addi %x8, 0, %x7
    beq %x3, 0, endl
    jmp loop
endl:
    end