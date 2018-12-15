DATA SEGMENT
	a DD
	b DD
	aux DD
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
debut_while1:
	mov eax, 0
	push eax
	mov eax, b
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
	jz sortie_while1
	mov eax, a
	push eax
	mov eax, b
	push eax
	pop eax
	pop ebx
	mov ecx, eax
	div ecx, ebx
	mul ebx, ecx
	sub eax, ebx
	push eax
	pop eax
	mov aux, eax
	push eax
	pop eax
	mov eax, b
	push eax
	pop eax
	mov a, eax
	push eax
	pop eax
	mov eax, aux
	push eax
	pop eax
	mov b, eax
	push eax
	jmp debut_while1
sortie_while1:
	pop eax
	mov eax, a
	out eax
	pop eax
CODE ENDS
