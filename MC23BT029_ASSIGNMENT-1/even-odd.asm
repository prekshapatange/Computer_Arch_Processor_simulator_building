    .data
a:
    12
    .text
main:
    load %x0, $a, %x3
    divi %x3, 2, %x4
    beq  %x31, 0, foo
    addi %x0, 1, %x10
    jmp exit
foo:
    subi %x0, 1, %x10
    end
exit:
    end
