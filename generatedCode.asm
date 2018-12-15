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
	mov eax, b
	push eax
	mov eax, a
	push eax
	pop eax
	pop ebx
	sub eax, ebx
	jl vrai_lt_1
	mov eax, 0
	jmp sortie_lt_1
vrai_lt_1:
	mov eax, 1
sortie_lt_1:
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
