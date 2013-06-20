begin
	integer I;
	integer array A(2:6);
	I:=4;
	A(3):=5;
	A(I):=I+A(3);
	A(I+1):=A(4)*A(3);
end
