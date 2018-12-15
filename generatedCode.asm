DATA SEGMENT
	a DD
	b DD
	aux DD
DATA ENDS
CODE SEGMENT
	in eax
	push eax	pop eax
	mov a, eax
	push eax
	pop eax
	in eax
	push eax	pop eax
	mov b, eax
	push eax
	pop eax
debut_while1
	mov eax, 0
	push eax
	mov eax, b
	pop ebx
	sub ebx, eax
	jle faux_gt_1
	mov eax, 1
	jmp sortie_gt_1
faux_gt_1
	mov eax, 0
sortie_gt_1
	jz sortie_while_0
	mov eax, 0
	push eax
	mov eax, b
	push eax
	mov eax, b
	push eax
	mov eax, a
	pop ebx
	mov ecx, eax
	div ecx, ebx
	mul ecx, ebx
	sub eax, ecx
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
	pop eax
	pop eax
	jmp debut_while_1
sortie_while_1
	pop eax
	mov eax, output
	push eax
	pop eax
	pop eax
	pop eax
CODE ENDS
