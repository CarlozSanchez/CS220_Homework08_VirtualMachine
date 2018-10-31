// push constant 777
@777
D=A
@SP
M=M+1
A=M-1
M=D

// pop temp 6
@6
D=A
@THAT
A=A+1
D=D+A
@R13
M=D
@SP
AM=M-1
D=M
@R13
A=M
M=D

// push temp 6
@6
D=A
@THAT
A=A+1
A=A+D
D=M
@SP
M=M+1
A=M-1
M=D

// pop local 10
@10
D=A
@LCL
D=D+M
@R13
M=D
@SP
AM=M-1
D=M
@R13
A=M
M=D

