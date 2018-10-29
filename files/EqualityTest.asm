// push constant 5
@5
D=A
@SP
M=M+1
A=M-1
M=D

// push constant 77
@77
D=A
@SP
M=M+1
A=M-1
M=D

//lt
@SP
AM=M-1
D=M
A=A-1
D=D-M
M=-1
@skip0
D;JGT
@SP
A=M-1
M=0
(skip0)

