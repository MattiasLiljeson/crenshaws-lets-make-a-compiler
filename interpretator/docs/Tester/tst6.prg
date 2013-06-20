begin
	! Ett program som innehåller en rekursiv function;
	integer function F(X); integer X;
	begin
		if X>0 then F:=X+F(X-1) else F:=0;
	end;
	integer I;
	I:=3;
	I:=I+F(2)*I+F(I);
end
