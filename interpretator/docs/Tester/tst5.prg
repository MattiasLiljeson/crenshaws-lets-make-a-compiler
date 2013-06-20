begin
	! Ett program som innehåller en function;
	integer function F(X); integer X; F:=X+1;
	integer I;
	I:=3;
	I:=I+F(2)*I+F(I);
end
