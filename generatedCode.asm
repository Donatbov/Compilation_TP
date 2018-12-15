DATA SEGMENT
	a DD
	b DD
DATA ENDS
CODE SEGMENT
	in eax
	push eax
	pop eax
	mov a, eax
	push eax
	pop eax
	in eax
	push eax
	pop eax
	mov b, eax
	push eax
	pop eax
	mov eax, 1
	push eax
	mov eax, 1
	push eax
	pop eax
	pop ebx
	add eax, ebx
	jnz debut_then1
	jmp debut_else1
debut_then1:
	mov eax, a
	out eax
	jmp fin_if1
debut_else1:
	mov eax, b
	out eax
	jmp fin_if1
fin_if1:
	pop eax

CODE ENDS
