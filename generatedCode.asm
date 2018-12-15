DATA SEGMENT
	prixTtc DD
	prixHT DD
DATA ENDS
CODE SEGMENT
	mov eax, 200
	push eax
	pop eax
	mov prixHT, eax
	push eax
	pop eax
	mov eax, prixHt
	push eax
	mov eax, 119
	push eax
	pop eax
	pop ebx
	mul eax, ebx
	push eax
	mov eax, 100
	push eax
	pop eax
	pop ebx
	div ebx, eax
	mov eax, ebx
	push eax
	pop eax
	mov prixTtc, eax
	push eax
	pop eax
CODE ENDS
